var readline = require('readline');

var rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

rl.on('line', function(line){ 
    silenceTimer.resetTiming();
})


    //用户未发言定时器
    var silenceTimer = {
        time1: 0, 
        time2: 0,
        timer1: 0,
        timer2: 0,
        objMsg1 : {
            type: "event",
            event: "clientsilence",
        },

        objMsg2 : {
            type: "event",
            event: "clientsilence",
        },

        init: function(_time1, _msg1, _time2, _msg2) {
            //把分钟转为秒
            this.time1 = _time1 * 60 * 100;
            this.time2 = _time2 * 60 * 100;
            this.objMsg1.msg = _msg1;
            this.objMsg2.msg = _msg2;
        },

        //计时结束后,发送超时提醒消息,并开始第二段计时.
         silenceTimer1 : function() {
            var that = this;
            return setTimeout(function() {
                console.log(that.objMsg1)
                that.timer2 = that.silenceTimer2();
            }, that.time1);
         },

        //计时结束后,发送超时提醒消息,并关闭连接.
         silenceTimer2 : function() {
            var that = this;
            return setTimeout(function() {
                console.log(that.objMsg2)
            }, that.time2)
         },

        startTiming : function() {
            this.timer1 = this.silenceTimer1();
        },

        resetTiming : function() {
            clearTimeout(this.timer1);
            clearTimeout(this.timer2);
            this.silenceTimer1();
        },

        clearTiming : function() {
            clearTimeout(this.timer1);
            clearTimeout(this.timer2);
        }
    };


    silenceTimer.init(1, "在不在?", 1, "我先挂了");
    var i = 0;
    setInterval(function() {
        console.log(++i);
    }, 1000);
    silenceTimer.startTiming();
    
