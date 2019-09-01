public class Object {

    // 立即释放引用
    public boolean release() {
        return false;
    }

    // 获取类型
    public String type() {
        return this.getClass().getTypeName();
    }

    // 检查类型
    public boolean typeOf(String name) {
        return this.getClass().getTypeName().equals(name);
    }
}
