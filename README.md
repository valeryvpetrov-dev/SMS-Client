# SMS Client
Application is able to receive incoming SMS messages, send SMS. All messages are stored in Realm database as hisotry. Advanced RecyclerView options such as different item types, item swipe are used to display messages.

## Usage
Demonstration video is available by link - https://drive.google.com/file/d/1u6RZ-aM7hW57ILlnNb8KV5IELXw7fS8J/view?usp=sharing

## References  
Android developers documentation:  
* Receive SMS permission - [https://developer.android.com/reference/android/Manifest.permission#RECEIVE_SMS](https://developer.android.com/reference/android/Manifest.permission#RECEIVE_SMS)
* Send SMS permission - 
[https://developer.android.com/reference/android/Manifest.permission.html#SEND_SMS](https://developer.android.com/reference/android/Manifest.permission.html#SEND_SMS)
* SMS receiver intent filter action - 
[https://developer.android.com/reference/android/provider/Telephony.Sms.Intents#SMS_RECEIVED_ACTION](https://developer.android.com/reference/android/provider/Telephony.Sms.Intents#SMS_RECEIVED_ACTION)
* RecyclerView DiffUtil - 
[https://developer.android.com/reference/android/support/v7/util/DiffUtil](https://developer.android.com/reference/android/support/v7/util/DiffUtil)
* RecyclerView swipte action implementation - [https://developer.android.com/reference/android/support/v7/widget/helper/ItemTouchHelper](https://developer.android.com/reference/android/support/v7/widget/helper/ItemTouchHelper)

Realm:
* Realm for Android - [https://realm.io/blog/realm-for-android/](https://realm.io/blog/realm-for-android/)
