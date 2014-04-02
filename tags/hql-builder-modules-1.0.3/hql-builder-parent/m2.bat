@echo off

set MAVEN_OPTS=-Xms64m -Xmx1024m -Xss2048k -Xmn32m -XX:PermSize=64m -XX:MaxPermSize=256m

:start
cls
echo %~dp0
echo ------------------
echo u. svn update
echo i. install (no tests)
echo e. eclipse (online)
echo o. eclipse (offline)
echo c. clean
echo d. download dependencies
echo b. rest build
echo r. release
echo y. update, deploy
echo s. generate sources
echo m. assembly
echo t. status
echo q. quit
echo ------------------
CHOICE /N /C:uieocdbrysmtq
set nr=%ERRORLEVEL%
IF %nr% EQU 1 goto lsvnup
IF %nr% EQU 2 goto llocinstall
IF %nr% EQU 3 goto leclipse
IF %nr% EQU 4 goto leclipseoff
IF %nr% EQU 5 goto lclean
IF %nr% EQU 6 goto ldependency
IF %nr% EQU 7 goto lrest
IF %nr% EQU 8 goto lrelease
IF %nr% EQU 9 goto ldeploy
IF %nr% EQU 10 goto lgen
IF %nr% EQU 11 goto lass
IF %nr% EQU 12 goto lstatus
IF %nr% EQU 13 goto lquit
goto start

:lstatus
cls
if exist status.bat goto lstatusbat
svn status .
pause
goto start

:lstatusbat
cls
call status.bat
goto start

:lsvnup
cls
if exist svnup.bat goto lsvnupbat
svn update
pause
goto start

:lsvnupbat
cls
call svnup.bat
goto start

:llocinstall
cls
call mvn -e install -DskipTests=true -DupdateReleaseInfo=true
pause
goto start

:leclipse
cls
call mvn -e -U -DdownloadSources=true -Dwtpversion=2.0 eclipse:eclipse
pause
goto start

:leclipseoff
cls
call mvn -e -U -DdownloadSources=true -Dwtpversion=2.0 eclipse:eclipse -o
pause
goto start

:lclean
cls
call mvn -e clean
pause
goto start

:ldependency
cls
call mvn -e dependency:go-offline
pause
goto start

:lrest
cls
call mvn -e -pl be.ugent.oasis:rest-xsd -am install -DskipTests=true
pause
goto start

:lrelease
cls
call mvn -e release:prepare
call mvn -e release:perform
pause
goto start

:ldeploy
cls
svn update
call mvn -e deploy
pause
goto start

:lgen
cls
call mvn -e generate-sources
pause
goto start

:lass
cls
call mvn -o -e package assembly:assembly
pause

:lquit
cls