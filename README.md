# bingwallpapers
saving daily bingwallpaper to your disk

# Usage

in Linux or macOS:

`java -jar /path/to/app.jar --saveFolder=/mnt/yourdisk/images`

in Windows cmd and powershell:

`java -jar /path/to/app.jar --saveFolder=d:\images`

in Windows Git Bash:

`java -jar /path/to/app.jar --saveFolder=/d/images`

use your own saveFolder please.

the output file name is like this: 2021-09-13.jpg

---

the jar file is only 454KB, you can run it as a crontab job for saving daily bingwallpaper to your local disk.

just type `crontab -e` then, add following line into the end of screen:

`0 12 * * * /usr/bin/java -jar /path/to/app.jar --saveFolder=/mnt/yourdisk/images`

This means saving pictures to the images folder at 12 noon every day.
