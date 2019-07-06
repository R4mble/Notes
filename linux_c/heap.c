#include <stdio.h>
#include <stdlib.h>

int *heap_array(int *old_ptr, int n);
void show_value(int *ptr);

int main() {
    int n;
    int *p = (int *)malloc(1 * sizeof(int));
    
    p[0] = 1;   //*p此时为1

    while (1) {
        if (scanf("%d", &n) == 0)
            break;

        p = heap_array(p, n);
        show_value(p);
    }
    free(p);
    return 0;
}

int *heap_array(int *old_ptr, int n) {
    int size = old_ptr[0] + 1;
    int *new_ptr;

    //将ptr所指向的堆内存大小扩展为size,返回扩展后的内存基地址
    new_ptr = (int *)realloc(old_ptr, (size * sizeof(int)));
    new_ptr[0] = size;
    
    //将新数据存入堆内存的尾端
    new_ptr[size - 1] = n;

    return new_ptr;
}

void show_value(int *ptr) {
    int i;
    printf("--->>>");
    for (int i=1; i<ptr[0]; i++) {
        printf("%d ", ptr[i]);
    }
    printf("<<<---\n");
}