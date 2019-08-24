#include <stdio.h>
#include <string.h>

struct student {
    char name[32];
    int age;
    float score;
};

void show(struct student *ptr_someone);

int main() {

    struct student jack;
    //圆点: 成员引用符
    strcpy(jack.name, "Jack");
    jack.age = 18;
    jack.score = 90.5;

    struct student rose = {"Rose", 16, 80.0};

    struct student michael = {
        .score = 88.5,
        .name = "Michael"
    };

    struct student Michael_Junior;
    Michael_Junior = michael;

    struct student myclass[50];
    myclass[0] = jack;
    myclass[1] = rose;
    myclass[2] = michael;
    myclass[3] = Michael_Junior;

    struct student *p;
    p = &jack;

    (*p).age = 23;
    p -> age = 24;

    show(&jack);

    return 0;
}

void show(struct student *ptr_someone) {
    printf("name: %s, age: %d, score: %f\n",
            ptr_someone -> name,
            ptr_someone -> age,
            ptr_someone -> score);
}

