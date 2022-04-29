var $p = {
    tempObj: function(obj) {
        return JSON.parse(JSON.stringify(obj));
    },
    getToken: function(key) {
        let cookieKey = key + "=";
        let result = "";
        const cookieArr = document.cookie.split(";");

        for(let i = 0; i < cookieArr.length; i++) {
            if(cookieArr[i][0] === " ") {
               cookieArr[i] = cookieArr[i].substring(1);
            }

            if(cookieArr[i].indexOf(cookieKey) === 0) {
                result = cookieArr[i].slice(cookieKey.length, cookieArr[i].length);
                return result;
            }
        }
       return result;
    },

    setToken: function(key, value, expiredays) {
        let todayDate = new Date();
        todayDate.setDate(todayDate.getDate() + expiredays); // 현재 시각 + 일 단위로 쿠키 만료 날짜 변경
        //todayDate.setTime(todayDate.getTime() + (expiredays * 24 * 60 * 60 * 1000)); // 밀리세컨드 단위로 쿠키 만료 날짜 변경
        document.cookie = key + "=" + escape(value) + "; path=/; expires=" + todayDate.toGMTString() + ";";
    },
    deleteToken: function(key) {
        $p.setToken(key, '');
    },
    clearError: function(form) {
        for(var k in form) {
            form[k] = "";
        }
    },
    get_active_list: function(temp) {
        for(var i=temp.length-1; i>=0; i--) {
            if(temp[i].is_selected == false) {
                temp.splice(i,1);
            }
        }
        return temp;
    },
    location_href: function(url) {
        window.location.href = url;
    },
    reload: function() {
        window.location.reload();
    },
    formatDate: function(i_date) {
        var date = new Date(i_date).format("yyyy-MM-dd");
        var time = new Date(i_date).format("HH:mm:ss");
        return {date: date, time: time}
    }
}
