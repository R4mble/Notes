is运算符
    检测一个表达式是否某类型的实例.如果是,在检测后可以直接当做该类型使用.
for循环
    普通的和索引遍历.
        for((k, v) in map) print("$k -> $v")
when表达式
    模式匹配, 可以匹配具体的值, 也可以匹配类型.
in运算符
    检测某个数字是否在指定区间内.
        for (x in 9 downTo 0 step 3) print(x)
lambda表达式
    filter, sortedBy, map, forEach使用大括号  
    list.filter{it > 0}
类的创建
    不同指定实例类型, 不用new     
DTO
    data class Customer(val name: String, val email: String)
    提供: getters, 对var还有setters
         equals(), hashCode(), toString(), copy()
默认参数
    fun foo(a: Int = 0, b: String = "") {...}
访问map
    map["key"]
延迟属性
    val p: String by lazy {}
扩展函数
    fun String.spaceToCamelCase() {...}
创建单例
    object Resource {
        val name = "Name"
    }

if not null缩写
    val files = File("Test").listFiles()
    print(files?.size)

if not null and else
    print(files?.size ?: "empty")

if null执行语句
    val email = values["email"] ?: throw IllegalStateException("Email is missing")

在可能会空的集合取第一个元素
    val mainEmail = emails.firstOrNull() ?: ""

if not null执行代码
    value?.let {}

可空布尔
    val b: Boolean? = ...

交换变量
    a = b.also {b = a}

三个等号与两个等号
    数字装箱的 同一性 相等性

基本类型
    在运行时表示为原生类型值,使用时像普通的类.
    较小类型不能隐式转换为较大的类型.

密封类
    表示受限的类继承结构, 当一个值为有限集中的类型,不能有任何其他类型时.

协程