tab1 = { key1 = "val1", key2 = "val2", "val3" }
for k, v in pairs(tab1) do
    print(k .. " - " .. v)
end

-- 对于全局变量和 table，nil 还有一个"删除"作用，给全局变量或者 table 表里的变量赋一个 nil 值，等同于把它们删掉
tab1.key1 = nil
for k, v in pairs(tab1) do
    print(k .. " - " .. v)
end

--nil 作比较时应该加上双引号
--type(X)==nil 结果为 false 原因是 type(type(X))==string
print(type(X)=="nil")

--Lua 把 false 和 nil 看作是"假"，其他的都为"真"
if false or nil then
    print("至少有一个是 true")
else
    print("false 和 nil 都为 false!")
end

x, y = y, x                     -- swap 'x' for 'y'


t = {'a'}
i = 'index'
t[i] = 55
print(t[i])
print(t.index)  -- 当索引为字符串类型时的一种简化写法
--gettable_event(t,index) -- 采用索引访问本质上是一个类似这样的函数调用

print(#t)

--pairs可以遍历表中所有的key，并且除了迭代器本身以及遍历表本身还可以返回nil;
--但是ipairs则不能返回nil,只能返回数字0，如果遇到nil则退出。它只能遍历到表中出现的第一个不是整数的key
--ipairs 在迭代过程中是会直接跳过所有手动设定key值的变量

mytable = setmetatable({ 10, 20, 30 }, {
    __tostring = function(mytable)
        local sum = 0
        for _, v in pairs(mytable) do
            sum = sum + v
        end
        return "表所有元素的和为 " .. sum
    end
})
print(mytable)

function table_maxn(t)
    local mn = 0
    for k, v in pairs(t) do
        if mn < k then
            mn = k
        end
    end
    return mn
end

-- 定义元方法__call
mytable = setmetatable({10}, {
    __call = function(mytable, newtable)
        local sum = 0
        for i = 1, table_maxn(mytable) do
            sum = sum + mytable[i]
        end
        for i = 1, table_maxn(newtable) do
            sum = sum + newtable[i]
        end
        return sum
    end
})
newtable = {10,20,30}
print(mytable(newtable))

mytable = setmetatable({ 1, 2, 3 }, {
    __add = function(mytable, newtable)
        for i = 1, table_maxn(newtable) do
            table.insert(mytable, table_maxn(mytable)+1,newtable[i])
        end
        return mytable
    end
})

secondtable = {4,5,6}

mytable = mytable + secondtable
for k,v in ipairs(mytable) do
    print(k,v)
end