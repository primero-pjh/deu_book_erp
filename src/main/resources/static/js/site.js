var $p = {
    tempObj: function(obj) {
        return JSON.parse(JSON.stringify(obj));
    },
    getToken: function(key) {
        return window.sessionStorage.getItem(key);
    },
    deleteToken: function(key) {
        window.sessionStorage.removeItem(key);
    },
    setToken: function(key, value) {
        window.sessionStorage.setItem(key, value);
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
    reload: function() {
        window.location.reload();
    },
    formatDate: function(i_date) {
        var date = new Date(i_date).format("yyyy-MM-dd");
        var time = new Date(i_date).format("HH:mm:ss");
        return {date: date, time: time}
    }
}
