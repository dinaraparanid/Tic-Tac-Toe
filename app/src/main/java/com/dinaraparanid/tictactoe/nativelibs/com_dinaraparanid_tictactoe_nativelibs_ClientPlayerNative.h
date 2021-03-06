/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_dinaraparanid_tictactoe_nativelibs_ClientPlayerNative */

#ifndef _Included_com_dinaraparanid_tictactoe_nativelibs_ClientPlayerNative
#define _Included_com_dinaraparanid_tictactoe_nativelibs_ClientPlayerNative
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_dinaraparanid_tictactoe_nativelibs_ClientPlayerNative
 * Method:    init
 * Signature: (Ljava/lang/String;)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_com_dinaraparanid_tictactoe_nativelibs_ClientPlayerNative_init
  (JNIEnv *, jclass, jstring);

/*
 * Class:     com_dinaraparanid_tictactoe_nativelibs_ClientPlayerNative
 * Method:    sendReady
 * Signature: (Ljava/nio/ByteBuffer;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_dinaraparanid_tictactoe_nativelibs_ClientPlayerNative_sendReady
  (JNIEnv *, jclass, jobject);

/*
 * Class:     com_dinaraparanid_tictactoe_nativelibs_ClientPlayerNative
 * Method:    sendMove
 * Signature: (Ljava/nio/ByteBuffer;BB)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_dinaraparanid_tictactoe_nativelibs_ClientPlayerNative_sendMove
  (JNIEnv *, jclass, jobject, jbyte, jbyte);

/*
 * Class:     com_dinaraparanid_tictactoe_nativelibs_ClientPlayerNative
 * Method:    readTable
 * Signature: (Ljava/nio/ByteBuffer;)[[B
 */
JNIEXPORT jobjectArray JNICALL Java_com_dinaraparanid_tictactoe_nativelibs_ClientPlayerNative_readTable
  (JNIEnv *, jclass, jobject);

/*
 * Class:     com_dinaraparanid_tictactoe_nativelibs_ClientPlayerNative
 * Method:    readCommand
 * Signature: (Ljava/nio/ByteBuffer;)B
 */
JNIEXPORT jbyte JNICALL Java_com_dinaraparanid_tictactoe_nativelibs_ClientPlayerNative_readCommand
  (JNIEnv *, jclass, jobject);

/*
 * Class:     com_dinaraparanid_tictactoe_nativelibs_ClientPlayerNative
 * Method:    readRole
 * Signature: (Ljava/nio/ByteBuffer;)B
 */
JNIEXPORT jbyte JNICALL Java_com_dinaraparanid_tictactoe_nativelibs_ClientPlayerNative_readRole
  (JNIEnv *, jclass, jobject);

/*
 * Class:     com_dinaraparanid_tictactoe_nativelibs_ClientPlayerNative
 * Method:    readState
 * Signature: (Ljava/nio/ByteBuffer;)B
 */
JNIEXPORT jbyte JNICALL Java_com_dinaraparanid_tictactoe_nativelibs_ClientPlayerNative_readState
  (JNIEnv *, jclass, jobject);

/*
 * Class:     com_dinaraparanid_tictactoe_nativelibs_ClientPlayerNative
 * Method:    drop
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_com_dinaraparanid_tictactoe_nativelibs_ClientPlayerNative_drop
  (JNIEnv *, jclass, jobject);

#ifdef __cplusplus
}
#endif
#endif
