@echo off
set JDK="C:\Program Files\Java\jdk1.5.0_19"
rem when i'm using cygwin with gcc bundled
rem JDK="/cygdriver/C/Program Files/Java/jdk1.5.0_19"
gcc -mno-cygwin -I %JDK%\include\win32 -I %JDK%\include -I %JDK% -Wl,--add-stdcall-alias -shared -o capture_message.dll captureMessage.c captureMessage.def
pause
echo Copying dll in java project
xcopy /Y capture_message.dll ..\..\lib\dll
rem gcc -c captureMessage.c -o captureMessage.o -I %JDK%\include
rem gcc -static -o capture_message.dll captureMessage.o
pause