

/**
 * 
 * #include <sys/types.h>
 * #include <sys/stat.h>
 * #include <fcntl.h>
 * 
 * int open(const char *pathname, int flags);
 * int open(const char *pathname, int flags, mode_t mode);
 * 
 * flgs: 只读, 只写, 读写, 追加... 多个flag用 | 隔开.
 * mode: 权限,  如 0644, 0755
 * 返回值: 文件描述符
 * 
 * 可以用来打开普通文件,块设备文件,字符设备文件,链接文件,管道文件,
 * 只能用来创建普通文件,每一种特殊文件的创建都有其特定的其他函数.
 * 
 * #include <unistd.h>
 * 
 * int close(int fd);
 * fd: 要关闭的文件的描述符.
 * 返回值: 成功0, 失败1.
 * 可以重复关闭已关闭或未打开的文件.
 */