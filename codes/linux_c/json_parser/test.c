
#include <stdio.h>

int main() {

unsigned char * p = (unsigned char *)414224528;

    for (int i=0; i<41; i++) {
        printf("%c", *(p + i));
    }
}
