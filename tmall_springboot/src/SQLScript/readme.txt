注： 不要用 navicat,mysql-front 等工具导入，因为数据量大，这些工具处理不了，会报奇奇怪怪的错误。
 
"D:\tools\MYSQL\mysql-5.1.57-win32\bin\mysql.exe" -u root -padmin --default-character-set=utf8 tmall_springboot < d:\tmall_springboot.sql
 
注: D:\tools\MYSQL\mysql-5.1.57-win32\bin\mysql.exe 这个路径是我的mysql.exe的路径，根据实际情况换成自己的mysql运行路径
注: tmall_springboot.sql下载后，放在 d:\tmall_springboot.sql 这个位置