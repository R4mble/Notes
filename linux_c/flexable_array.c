#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <strings.h>

struct package {
    int msg_len;
    //零长数组
    char msg[0];
};

void test_package(struct package *p);

int main() {
    char buffer[100];
    
    while (1) {
        //从指定的流读取一行，存在buffer 所指向的字符串内。当读取 (n-1) 个字符时，或者读取到换行符时，或者到达文件末尾时，它会停止
        fgets(buffer, 100, stdin);

        struct package *p = malloc(sizeof(struct package)
                        + strlen(buffer)
                        + 1);

        //总为4
        printf("%d\n", sizeof(struct package));
        //输入字符数加一
        printf("%d\n", strlen(buffer));
        
        p->msg_len = strlen(buffer) + 1;
        //将指定长度的字符串复制到字符数组中
        strncpy(p->msg, buffer, p->msg_len);

        test_package(p);             
    }
    return 0;
}

void test_package(struct package *p) {
    printf("total len: %d\n", sizeof(struct package)
                + p->msg_len);
    printf("message: %s", p->msg);            
}



