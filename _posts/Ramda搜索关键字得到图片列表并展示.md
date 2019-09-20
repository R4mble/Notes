---
title: Ramda搜索关键字得到图片列表并展示
date: 2019-04-03 14:31:59
tags: [函数式编程]
categories: [JavaScript]
---

```javascript
requirejs.config({
    paths: {
        ramda: 'https://cdnjs.cloudflare.com/ajax/libs/ramda/0.13.0/ramda.min',
        jquery: 'https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min'
    }
});

require([
    'ramda',
    'jquery',
],
    function (_, $) {

        let Impure = {
            getJSON: _.curry(function (callback, url) {
                $.getJSON(url, callback);
            }),

            setHtml: _.curry(function (sel, html) {
                $(sel).html(html)
            })
        };

        let img = function (url) {
            return $('<img />', { src: url});
        };

        let trace = _.curry(function (tag, x) {
            console.log(tag, x);
            return x;
        });

        let url = function (term) {
            return 'https://api.flickr.com/services/feeds/photos_public.gne?tags=' + term + '&format=json&jsoncallback=?';
        };

        let mediaUrl = _.compose(_.prop('m'), _.prop('media'));

        // let srcs = _.compose(_.map(mediaUrl), _.prop('items'));
        // let images = _.compose(_.map(img), srcs);

        //上面2行有两个map, 循环了两边.  优化写法:
        let mediaToImg = _.compose(img, mediaUrl);
        let images = _.compose(_.map(mediaToImg), _.prop('items'));

        let renderImages = _.compose(Impure.setHtml("body"), images);

        let app = _.compose(Impure.getJSON(renderImages), url);

        app("star");

});





```