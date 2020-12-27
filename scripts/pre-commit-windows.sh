#!C:/Program\Files/Git/usr/bin/sh.exe

echo "Running lint check..."

./gradlew app:ktlintCheck --daemon

status=$?

# return 1 exit code if running checks fails
[ $status -ne 0 ] && exit 1
exit 0