@echo off

set sDate=%date:~0,4%%date:~5,2%%date:~8,2%
for /f "tokens=1,2 delims=- " %%A in ('time /t') do set myTime=%%A%%B
set myTime1=%myTime:~0,2%%myTime:~3,2%
set myTime2=%time:~6,2%
set sTime=%myTime1%%myTime2%
adb shell "cat /proc/cpuinfo | grep -E 'Processor|Hardware'"> %cd%\cputhread-%sDate%-%sTime%.txt
set /p t=������������ʱ��(s): 
echo Sampling interval : %t% >> %cd%\cputhread-%sDate%-%sTime%.txt
echo=
echo ��������ʱ�䣺%sDate%-%sTime%
echo ��ʼִ��adb shell "top -d %t% -s cpu -t | grep %1$" ...
echo=
echo ����ֹͣ���ԣ���ֱ�ӹرձ����ڣ�
echo=>> %cd%\cputhread-%sDate%-%sTime%.txt
echo=>> %cd%\cputhread-%sDate%-%sTime%.txt
:A
set sDateNew=%date:~0,4%%date:~5,2%%date:~8,2%
for /f "tokens=1,2 delims=- " %%A in ('time /t') do set myTimeNew=%%A%%B
set myTime1New=%myTimeNew:~0,2%%myTimeNew:~3,2%
set myTime2New=%time:~6,2%
set sTimeNew=%myTime1New%%myTime2New%
echo  Timestamp: %sDateNew%-%sTimeNew% >> %cd%\cputhread-%sDate%-%sTime%.txt
echo  PID   TID PR CPU S     VSS     RSS PCY UID      Thread          Proc >> %cd%\cputhread-%sDate%-%sTime%.txt
adb shell "top -n 1 -d %t% -s cpu -t | grep %1$" >> %cd%\cputhread-%sDate%-%sTime%.txt
goto A
