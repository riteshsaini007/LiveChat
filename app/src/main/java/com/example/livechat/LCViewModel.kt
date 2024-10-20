package com.example.livechat

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.example.livechat.data.CHATS
import com.example.livechat.data.ChatData
import com.example.livechat.data.ChatUser
import com.example.livechat.data.Event
import com.example.livechat.data.MESSAGE
import com.example.livechat.data.Message
import com.example.livechat.data.STATUS
import com.example.livechat.data.Status
import com.example.livechat.data.USER_NODE
import com.example.livechat.data.UserData
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class LCViewModel @Inject constructor(
    val auth: FirebaseAuth,
    var db: FirebaseFirestore,
    val storage: FirebaseStorage
) : ViewModel() {

    var inProcess = mutableStateOf(false)
    var inProcessChats = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    val chats = mutableStateOf<List<ChatData>>(listOf())
    val chatMessages = mutableStateOf<List<Message>>(listOf())
    val inProgressChatMessage = mutableStateOf(false)
    var currentChatMessageListener: ListenerRegistration? = null


    val status = mutableStateOf<List<Status>>(listOf())
    val inProgressStatus = mutableStateOf(false)

    init {
        val currentUser = auth.currentUser
        signIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)
        }
    }


    fun populateMessage(chatId: String) {
        inProgressChatMessage.value = true
        currentChatMessageListener = db.collection(CHATS).document(chatId).collection(MESSAGE)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    handleException(error)

                }
                if (value != null) {
                    chatMessages.value = value.documents.mapNotNull {
                        it.toObject<Message>()
                    }.sortedBy { it.timestamp }
                    inProgressChatMessage.value = false
                }
            }
    }


    fun depopulateMessage() {
        chatMessages.value = listOf()
        currentChatMessageListener = null
    }


    fun populatesChats() {
        inProcessChats.value = true
        db.collection(CHATS).where(
            Filter.or(
                Filter.equalTo("user1.userId", userData.value?.userId),
                Filter.equalTo("user2.userId", userData.value?.userId),
            )
        ).addSnapshotListener { value, error ->

            if (error != null) {
                handleException(error)
            }

            if (value != null) {
                chats.value = value.documents.mapNotNull {
                    it.toObject<ChatData>()
                }
                inProcessChats.value = false
            }
        }
    }


    fun onSendReply(chatID: String, message: String) {
        val time = Calendar.getInstance().time.toString()

        val msg = Message(userData.value?.userId, message, time)
        db.collection(CHATS).document(chatID).collection(MESSAGE).document().set(msg)

    }


    fun signup(name: String, number: String, email: String, password: String) {
        inProcess.value = true
        if (name.isEmpty() || number.isEmpty() || email.isEmpty() || password.isEmpty()) {
            handleException(customMessage = "Please fill all fields")
            inProcess.value = false // Set inProcess to false
            return
        }

        db.collection(USER_NODE).whereEqualTo("number", number).get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                signIn.value = true
                                createOrUpdateProfile(name, number)
                            } else {
                                handleException(task.exception, customMessage = "Sign Up failed")
                            }
                        }
                } else {
                    handleException(customMessage = "Number Already Exists")
                    inProcess.value = false
                }
            }.addOnFailureListener { e ->
                handleException(e, "Failed to check for existing number")
                inProcess.value = false
            }
    }

    fun loginIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            handleException(customMessage = "Please fill all fields")
            return
        }

        inProcess.value = true
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                signIn.value = true
                auth.currentUser?.uid?.let {
                    getUserData(it)
                }
            } else {
                handleException(task.exception, customMessage = "Login failed")
            }
            inProcess.value = false // Move outside success block to always stop the loader
        }
    }

    fun uploadProfileImage(uri: Uri) {
        uploadImage(uri) {
            createOrUpdateProfile(imageurl = it.toString())
        }
    }

    fun uploadImage(uri: Uri, onSuccess: (Uri) -> Unit) {
        inProcess.value = true
        val storageRef = storage.reference
        val uuid = UUID.randomUUID()
        val imageRef = storageRef.child("images/$uuid")
        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                onSuccess(uri)
                inProcess.value = false // Stop loader
            }
        }.addOnFailureListener { e ->
            handleException(e)
            inProcess.value = false // Stop loader
        }
    }

    fun createOrUpdateProfile(
        name: String? = null,
        number: String? = null,
        imageurl: String? = null
    ) {
        val uid = auth.currentUser?.uid
        val newUserData = UserData(
            userId = uid,
            name = name ?: userData.value?.name,
            number = number ?: userData.value?.number,
            imageUrl = imageurl ?: userData.value?.imageUrl
        )

        uid?.let {
            inProcess.value = true
            db.collection(USER_NODE).document(uid).get().addOnSuccessListener { document ->
                if (document.exists()) {
                    db.collection(USER_NODE).document(uid).set(newUserData)
                    getUserData(uid)
                } else {
                    db.collection(USER_NODE).document(uid).set(newUserData)
                    getUserData(uid)
                }
                inProcess.value = false
            }.addOnFailureListener { e ->
                handleException(e, "Cannot retrieve user")
                inProcess.value = false // Always stop loader
            }
        }
    }

    private fun getUserData(uid: String) {
        inProcess.value = true
        db.collection(USER_NODE).document(uid).addSnapshotListener { snapshot, e ->
            if (e != null) {
                handleException(e, "Cannot retrieve user")
                inProcess.value = false
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d("Firestore", "User data: ${snapshot.data}")
                val user = snapshot.toObject<UserData>()
                if (user != null) {
                    userData.value = user
                }
                inProcess.value = false // Stop loader
                populatesChats()
                populateStatuses()
            } else {
                handleException(customMessage = "User data not found")
                inProcess.value = false // Stop loader
            }
        }
    }

    fun handleException(exception: Exception? = null, customMessage: String = "") {
        Log.e("LiveChatApp", "Live chat exception", exception)
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: "Unknown error"
        val message = if (customMessage.isEmpty()) customMessage else errorMsg

        eventMutableState.value = Event(message)
        inProcess.value = false
    }

    fun logout() {
        auth.signOut()
        signIn.value = false
        userData.value = null
        depopulateMessage()
        currentChatMessageListener = null
        eventMutableState.value = Event("Logged Out")
    }

    fun onAddChat(number: String) {
        if (number.isEmpty() or !number.isDigitsOnly()) {
            handleException(customMessage = "Number must be contain digit only")
        } else {
            db.collection(CHATS).where(
                Filter.or(
                    Filter.and(
                        Filter.equalTo("user1.number", number),
                        Filter.equalTo("user2.number", userData.value?.number)
                    ),
                    Filter.and(
                        Filter.equalTo("user1.number", userData.value?.number),
                        Filter.equalTo("user2.number", number)
                    )
                )
            ).get().addOnSuccessListener {
                if (it.isEmpty) {
                    db.collection(USER_NODE).whereEqualTo("number", number).get()
                        .addOnSuccessListener {
                            if (it.isEmpty) {
                                handleException(customMessage = "Number Not found")
                            } else {
                                val chatPartner = it.toObjects<UserData>()[0]
                                val id = db.collection(CHATS).document().id
                                val chat = ChatData(
                                    chatId = id,
                                    ChatUser(
                                        userData.value?.userId,
                                        userData.value?.name,
                                        userData.value?.imageUrl,
                                        userData.value?.number
                                    ),
                                    ChatUser(
                                        chatPartner.userId,
                                        chatPartner.name, chatPartner.imageUrl,
                                        chatPartner.number
                                    )
                                )
                                db.collection(CHATS).document(id).set(chat)
                            }
                        }
                        .addOnFailureListener {
                            handleException(it)
                        }
                } else {
                    handleException(customMessage = "Chat already exists")
                }
            }
        }


    }

    fun uploadStatus(uri: Uri) {
        uploadImage(uri) {
            createStatus(it.toString())
        }

    }


    fun createStatus(imageurl: String) {
        val newStatus = Status(
            ChatUser(
                userData.value?.userId,
                userData.value?.name,
                userData.value?.imageUrl,
                userData.value?.number,
            ),
            imageurl,
            System.currentTimeMillis()
        )
        db.collection(STATUS).document().set(newStatus)
    }



    fun populateStatuses() {
           inProgressStatus.value = true

           val currentUser = userData.value?.userId
           if (currentUser == null) {
               handleException(customMessage = "User not logged in")
               return
           }

           // Retrieve all chat participants for the current user
           db.collection(CHATS)
               .where(
                   Filter.or(
                       Filter.equalTo("user1.userId", currentUser),
                       Filter.equalTo("user2.userId", currentUser)
                   )
               )
               .get()
               .addOnSuccessListener { chatSnapshot ->
                   val currentConnections = arrayListOf(currentUser)

                   val chats = chatSnapshot.toObjects<ChatData>()
                   chats.forEach { chat ->
                       if (chat.user1.userId == currentUser) {
                           chat.user2.userId?.let { currentConnections.add(it) }
                       } else {
                           chat.user1.userId?.let { currentConnections.add(it) }
                       }
                   }

                   // Now retrieve statuses of all users in currentConnections
                   db.collection(STATUS)
                       .whereIn("user.userId", currentConnections)
                       .addSnapshotListener { statusSnapshot, statusError ->
                           if (statusError != null) {
                               handleException(statusError)
                               return@addSnapshotListener
                           }

                           if (statusSnapshot != null) {
                               status.value = statusSnapshot.toObjects()
                           }

                           inProgressStatus.value = false
                       }
               }
               .addOnFailureListener { e ->
                   handleException(e, "Failed to retrieve chats")
                   inProgressStatus.value = false
               }
       }



}