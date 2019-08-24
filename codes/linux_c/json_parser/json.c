#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdio.h>


struct JsonObject {
    
};

int main() {
    int json_fd = open("example.json", O_RDONLY);

    char *p;
    int file_byte = read(json_fd, p, 1000);

    printf("json size: %d bytes\n\n", file_byte);

    for (int i=0; i<file_byte; i++) {
        printf("%c", *(p + i));
    }

    close(json_fd);

    return 0;
}

