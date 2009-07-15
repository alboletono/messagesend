#include <iostream>
#include <windows.h>
#include <lm.h>
#include <time.h>

#include <Winnls.h>
#include <stdio.h>
using namespace std;

void construireMessage(char **, char *);
void test(void);

int main(int argc, char *argv[])
{

   #pragma comment(lib,"NETAPI32")

   /* variable */
   NET_API_STATUS iRes = 0;   
   WCHAR wDomain[256];
   WCHAR wDestination[256];
   WCHAR wSource[256];
   char *cMessage =  NULL;
   char **cTemp = NULL;
   char *cDest = NULL;
   char *cSource = NULL;
   int iNbParam = 0;
   int iCptParamMessage = 0;


   if(argc == 1){
      printf("\n - message - Nom destinataire - nom de la source -");
   }
   else{
      
      iNbParam = argc - 1;
      /* Recupere les parametre */
      cDest = argv[iNbParam-1];
      cSource = argv[iNbParam];

      /* Creation du message */
      for(iCptParamMessage = 1; iCptParamMessage < iNbParam - 1 ;iCptParamMessage++){
         
         construireMessage(&cMessage,argv[iCptParamMessage]);  
         /* Espace entre chaque mot */
         construireMessage(&cMessage," "); 
      }  
      
      /* Transformation des char * en WCHAR (char sur 2 octets) */
      MultiByteToWideChar( CP_ACP, 0, cMessage,strlen(cMessage)+1, wDomain, sizeof(wDomain)/sizeof(wDomain[0]) );
      MultiByteToWideChar( CP_ACP, 0, cDest,strlen(cDest)+1, wDestination, sizeof(wDestination)/sizeof(wDestination[0]) );
      MultiByteToWideChar( CP_ACP, 0, cSource,strlen(cSource)+1, wSource, sizeof(wSource)/sizeof(wSource[0]) );

      iRes = NetMessageNameAdd(NULL,wSource);

      // ,a qui -- nom source -- message -- taille msg
      iRes = NetMessageBufferSend(NULL, wDestination, wSource, (BYTE*)&wDomain[0], wcslen(wDomain) * 2);
   
      if(iRes == NERR_Success) printf("\nok");
      if(iRes == ERROR_INVALID_PARAMETER ) printf(" \n    The specified parameter is invalid.");
      if(iRes == NERR_AlreadyExists  )     printf(" \n 	The message alias already exists on this computer. For more information, see the following Remarks section.");
      if(iRes == NERR_DuplicateName )      printf(" \n  	The name specified is already in use as a message alias on the network.");
      if(iRes == NERR_NetworkError  )      printf(" \n  	A general failure occurred in the network hardware.");
      if(iRes == NERR_TooManyNames  )      printf(" \n  	The maximum number of message aliases has been exceeded.");

      iRes = NetMessageNameDel(NULL,wSource);
   }
   return 0;
}

void construireMessage(char **message, char *mot )
{

   if (*message == NULL) {
       
   	*message = (char *) malloc ((strlen(mot) + 1) * sizeof(char));
      if (*message == NULL) return;
	   strcpy(*message,mot);

   }
   else{
              
	   *message = (char *) realloc (*message,(strlen(*message)+strlen(mot)+1) * sizeof(char));
	   if (*message == NULL) return;
	   strcat(*message,mot);
   }
}
