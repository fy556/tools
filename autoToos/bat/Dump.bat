@echo off
echo=
set /p t=请输入要导出的进程名: 
set sDate=%date:~0,4%%date:~5,2%%date:~8,2%
for /f "tokens=1,2 delims=- " %%A in ('time /t') do set myTime=%%A%%B
set myTime1=%myTime:~0,2%%myTime:~3,2%
set myTime2=%time:~6,2%
set sTime=%myTime1%%myTime2%
echo=
echo 准备生成 .hprof 文件...
adb shell "mkdir -p /data/local/tmp/hprof/"
adb shell "am dumpheap %t% /data/local/tmp/hprof/%t%_%sDate%-%sTime%.hprof"
choice /t 10 /d y /n >nul
echo=
echo 正在导出 .hprof 文件...
adb pull /data/local/tmp/hprof  .\HPROF
adb shell "rm -rf /data/local/tmp/hprof"
 .\config\hprof-conv  -z  .\HPROF\%t%_%sDate%-%sTime%.hprof  .\HPROF\%t%_%sDate%-%sTime%-z.hprof
echo=
echo .hprof 文件导出完毕！
echo=
pause
