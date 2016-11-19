//
// Created by ienning on 16-11-18.
//
#include "com_example_ienning_rxjavaandretrofitdemo_Testso.h"
#include <dlfcn.h>
#include <errno.h>
#include <limits.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#define LOG_TAG "CVE_2015_1528"
#define LOG_D(...) do{ __android_log_print( ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__); printf( __VA_ARGS__ ); }while( 0 )
/*
int Check_CVE_2015_1528()
{
    const char *libname = "testso.so";
    size_t * ( *native_handle_create )( int numFds, int numInts ) = NULL;
    void *handle = dlopen( libname, RTLD_NOW | RTLD_GLOBAL );
    if( !handle )
    {
        printf( "error opening %s: %s\n", libname, dlerror() );
        return -1;
    }
    native_handle_create = dlsym( handle, "native_handle_create" );
    if( !native_handle_create )
    {
        printf( "missing native_handle_create\n" );
        return -2;
    }

    int ret = -3;

    int numFds = 1025;
    int numInts = 1;
    size_t *bla = native_handle_create( numFds, numInts );
    if( !bla )
    {
        // fixed
        printf( "looks fixed to me\n" );
        ret = 0;
        goto done;
    }

    // sanity checks
    switch(bla[0])// version
    {
    case 12://android wear 5.0.2 LWX49K
        if( bla[1] != numFds || bla[2] != numInts )
        {
            LOG_D( "got back unexpected values\n" );
        }
        else
        {
            LOG_D( "its vulnerable\n" );
            return 1;
        }
        break;
    default:
        LOG_D( "failed.  version %d  %d %d\n", bla[0], bla[1], bla[2] );
        break;
    }


done:
    // done with this
    dlclose( handle );

    // should be allocated with malloc
    //! if its already null, then free does nothing
    free( bla );

    return ret;
}
*/
void Check_CVES_2015_1528()
{
    void *handle = dlopen("/data/data/com.example.ienning.rxjavaandretrofitdemo/lib/testso.so", RTLD_NOW | RTLD_GLOBAL);
    //void (*enter_shell)(int*);
    //int (*do_work)(int, int*);
    //enter_shell = dlsym(handle, "enter_shell");
    //do_work = dlsym(handle, "do_work");
    void (*test)();
    test = (void (*)()) dlsym(handle, "test");
    test();
    /*
    char buffer[4096];
    int pipetoserver[2];
    int pipefromserver[2];
    if(pipe(pipetoserver) == -1 || pipe(pipefromserver) == -1) {
        perror("pipe");
        exit(EXIT_FAILURE);
    }
    int pipein[2] = { pipetoserver[0], pipefromserver[1]};
    int pipeout[2] = {
        pipefromserver[0], pipetoserver[1]
    };
    int pid = fork();
    if(pid==0) {
        enter_shell(pipeout);
    }
    while(true) {
        do_work(1, NULL);
        printf("Now is test");
        sleep(2);
        do_work(2, pipein);
        sleep(1);
    }
    */

}
JNIEXPORT jstring JNICALL Java_com_example_ienning_rxjavaandretrofitdemo_Testso_get(JNIEnv *env, jobject thiz) {
    Check_CVES_2015_1528();
    return env->NewStringUTF("Hello Ienning JNI!");
}
JNIEXPORT void JNICALL Java_com_example_ienning_rxjavaandretrofitdemo_Testso_set(JNIEnv *env, jobject thiz, jstring str) {

}
