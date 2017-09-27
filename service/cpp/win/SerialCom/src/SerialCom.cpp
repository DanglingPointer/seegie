// SerialCom.cpp : Defines the exported functions for the DLL application.
//

#include "stdafx.h"
#include "SerialPort.h"

/*
* Class:     bci_SerialPort_Native
* Method:    getAllPorts
* Signature: ()[Ljava/lang/String;
*/
JNIEXPORT jobjectArray JNICALL Java_bci_SerialPort_00024Native_getAllPorts(JNIEnv *, jclass)
{
   return nullptr;
}

/*
* Class:     bci_SerialPort_Native
* Method:    initialize
* Signature: (Ljava/lang/String;)J
*/
JNIEXPORT jlong JNICALL Java_bci_SerialPort_00024Native_initialize(JNIEnv *env, jclass, jstring jport)
{
   const char *cport = env->GetStringUTFChars(jport, 0);
   SerialPort *p = new SerialPort(cport);
   env->ReleaseStringUTFChars(jport, cport);
   return reinterpret_cast<jlong>(p);
}

/*
* Class:     bci_SerialPort_Native
* Method:    setBaudRate
* Signature: (JI)V
*/
JNIEXPORT void JNICALL Java_bci_SerialPort_00024Native_setBaudRate(JNIEnv *, jclass, jlong, jint)
{

}

/*
* Class:     bci_SerialPort_Native
* Method:    read
* Signature: (J)[B
*/
JNIEXPORT jstring JNICALL Java_bci_SerialPort_00024Native_read(JNIEnv *, jclass, jlong)
{
   return nullptr;
}

/*
* Class:     bci_SerialPort_Native
* Method:    write
* Signature: (J[B)V
*/
JNIEXPORT void JNICALL Java_bci_SerialPort_00024Native_write(JNIEnv *, jclass, jlong, jstring)
{

}

/*
* Class:     bci_SerialPort_Native
* Method:    finalize
* Signature: (J)V
*/
JNIEXPORT void JNICALL Java_bci_SerialPort_00024Native_finalize(JNIEnv *, jclass, jlong jptr)
{
   SerialPort *p = reinterpret_cast<SerialPort *>(jptr);
   if (p) delete p;
}