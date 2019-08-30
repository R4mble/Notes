local f = require 'fun'
-- Lua函数式库测试

-- iter: 可以遍历数组,map,字符串和(gen,param,state)
-- iter(array)
-- iter(map)
-- iter(string)
-- iter(gen, param, state)

-- 1.遍历数组, 第一个是序号,从1开始.
for _, a in f.iter({1,2,3}) do print(_, a) end
print('============')

-- 2.遍历字符串
for _, a in f.iter("abcde") do print(_, a) end
print('============')

-- 3.遍历map
for _, k, v in f.iter({a=1, b=2, c=3}) do print(k, v) end
print('============')


local function mypairs_gen(max, state) 
    if (state >= max) then
        return nil
    end
    return state + 1, state + 1
end

local function mypairs(max)
    return mypairs_gen, max, 0
end

-- 4.遍历(gen,param,state)
for _, a in f.iter(mypairs(10)) do print(a) end
print('============')


-- each(fun, gen, param, state), 对遍历的每一个值执行fun函数.
f.each(print, {a=1, b=2, c=3})
print('============')

f.each(print, {1,2,3})
print('============')








