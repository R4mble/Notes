终端工具:
    elm repl
        交互. 编译代码到JS, 使用Node.js运行

    elm reactor
        运行elm项目

    elm make
        构建elm项目
            elm make Main.elm --output=main.html
            elm make Main.elm --output=mian.js
```javascript
<!DOCTYPE HTML>
<html>
<head>
  <meta charset="UTF-8">
  <title>Main</title>
  <link rel="stylesheet" href="whatever-you-want.css">
  <script src="main.js"></script>
</head>

<body>
  <div id="elm"></div>
  <script>
  var app = Elm.Main.init({
    node: document.getElementById('elm')
  });
  </script>
</body>
</html>
```                

    elm install
        安装依赖.
            elm install elm/json

语言基础:
    值:
        字符串拼接: ++
        浮点数除法: /       9 / 2 == 4.5
        整数除法: //       9 // 2 == 4
    函数:
        isNegative n = n < 0
    匿名函数:
        \n -> n < 0
    数据结构:
        List, Tuples, Records
        Record更新: { model | content = newContent }

Elm架构:
    Model:  应用状态
    Update: 更新状态
    View:   展示状态

```elm
import Html exposing (..)

-- MODEL
type alias Model = { ... }

-- UPDATE   a set of messages that we will get from the UI
type Msg = Reset | ...

update : Msg -> Model -> Model
update msg model =
  case msg of
    Reset -> ...
    ...

-- VIEW
view : Model -> Html Msg
view model =
  ...

```

用户输入: Buttons, Text Fields, Check Boxes, Radio Buttons.

                 Html
    Elm         ----->
Browser.sandbox         Runtime System --- DOM
                <-----
                  Msg

                 Html
                Cmd/Sub
    Elm         ----->                 --- HTTP
Browser.element         Runtime System --- DOM
                <-----                 --- Randomness
                  Msg                  --- Current Time

command the runtime system to make an HTTP request or
    generate a random number.
subscribe to the current time.

Json处理:
    单个属性读取:
        nameDecoder = field "name" string
    嵌套属性读取:
        gifDecoder = field "data" (field "image_url" string)
    多个属性读取:
        type alias Person = {name : String, age : Int}
        personDecoder = map2 Person (field "name" string) (field "age" int)
 
 与JavaScript的互操作
    Flags
    Ports

页面跳转:
    Browser.application


