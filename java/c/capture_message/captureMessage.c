#include <windows.h>
#include <stdio.h>
#include <jni.h>
#include "captureMessage.h"


int main() {
    return 0;
}

/**
 * Fetches the text.
 * Closes the window.
 * @return cText, the text fetched or NULL if none.
 */
JNIEXPORT jstring JNICALL Java_fr_albin_jmessagesend_message_CaptureMessage_getMessageText
    (JNIEnv * env, jobject object) {

   HWND     hText;
   HWND     hWindow;
   HWND     hButton;
   char *  cText;
   jstring  string;
   int     iLength;

   hText    = NULL;
   hWindow  = NULL;
   hButton  = NULL;
   cText    = NULL;
   string   = NULL;
   iLength  = 0;

   hWindow = FindWindow(NULL, "Service Affichage des messages ");

   /* We got the handle. */
   if (hWindow != NULL) {

      /* Gets text control. */
      hText = FindWindowEx(hWindow, 0, "Static", NULL);
      if (hText != NULL) {

         /* Gets the text length */
         iLength = (int) SendMessage(hText, WM_GETTEXTLENGTH, 0, 0);
         iLength = iLength + 1;

         /* Alloc the text. */
         cText = malloc(sizeof(char) * iLength);
         memset(cText, 0, sizeof(char));

         /* Gets the text. */
         SendMessage(hText, WM_GETTEXT, (WPARAM) iLength, (LPARAM) cText);

         /* Gets ok button control. */
         hButton = FindWindowEx(hWindow, 0, "Button", NULL);

         /* Clicks on it. */
         if (hButton != NULL) {
            SendMessage(hWindow, WM_CLOSE, 0, 0);
         }
         string = (*env)-> NewStringUTF(env, cText);
         free(cText);
      }
   }
   return string;
}
