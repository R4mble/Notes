#include <stdio.h>
int main () {
  printf("%c",checkCPU());
}

int checkCPU(void) {
    union w {
        int a;
        char b;
    } c;
    c.a = 1;
    return (c.b == 1);
}
