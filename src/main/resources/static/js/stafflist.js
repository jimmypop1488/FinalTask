function getIndex(list, staffid) {
    for (var i = 0; i < list.length; i++) {
        if (list[i].staffid === staffid) {
            return i;
        }
    }

    return -1;
}

var staffApi = Vue.resource('/staff{/staffid}')

Vue.component('staff-form', {
    props: ['staffs', 'staffAttr'],
    data: function () {
        return {
            staffid: '',
            staffname: '',
            headstaffid: '',
            stafforgid: ''
        }
    },
    watch: {
        staffAttr: function(newVal, oldVal) {
            this.staffid = newVal.staffid;
            this.staffname = newVal.staffname;
            this.stafforgid = newVal.stafforgid;
            this.headstaffid = newVal.headstaffid;
        }
    },
    template: '<table cellspacing="0" cellpadding="0"><tr><td><table cellspacing="0" cellpadding="0"><tr>' +
        '<td><input type="text" placeholder="Работник" v-model="staffname" /></td>' +
        '<td><input type="text" placeholder="Организация" v-model="stafforgid" /></td>' +
        '<td><input type="text" placeholder="Начальник" v-model="headstaffid" /></td>' +
        '<td><input type="button" value="Сохранить" @click="save" /></td>' +
        '</tr></table>' +
        '<table bordercolor="black" border="1px" cellspacing="0" cellpadding="0"><tr>' +
        '<td width="100px"><b>№</b></td>' +
        '<td width="200px"><b>Имя Работника</b></td>' +
        '<td width="200px"><b>Название Организации</b></td>' +
        '<td width="200px"><b>Имя Начальника</b></td>' +
        '<td width="145px"><b></b></td>' +
        '</tr></table></td></tr></table>',
    methods: {
        save: function () {
            var staff = {
                staffid: "none",
                staffname: this.staffname,
                stafforgid: this.stafforgid,
                headstaffid: this.headstaffid
            };
            console.log(staff);
            if (this.staffid) {
                staffApi.update({staffid: this.staffid}, staff).then(result =>
                    result.json().then(data => {
                        var index = getIndex(this.staffs, data.staffid);
                        this.staffs.splice(index, 1, data);
                        this.staffid = '';
                        this.staffname = '';
                        this.stafforgid = '';
                        this.headstaffid = ''
                    })
                )
            } else {
                staffApi.save({}, staff);
                window.location.reload();
            }
        }
    }
});

Vue.component('staff-row', {
    props: ['staff', 'staffs', 'editStaff'],
    data: function() {
        return {
            ind: getIndex(this.staffs, this.staff.staffid) + 1
        }
    },
    template: '<table bordercolor="black" border="1px" cellspacing="0" cellpadding="0">' +
        '<tr><td width="100px">{{ ind }}</td>' +
        '<td width="200px">{{ staff.staffname }}</td>' +
        '<td width="200px">{{ staff.stafforgid }}</td>' +
        '<td width="200px">{{ staff.headstaffid }}</td>' +
        '<td width="145px">' +
        '<input type="button" value="Изменить" @click="edit" />' +
        '<input type="button" value="Удалить" @click="del" /></td>' +
        '</tr></table>',
    methods: {
        edit: function () {
            this.editStaff(this.staff);
        },
        del: function () {
            staffApi.remove({staffid: this.staff.staffid}).then(result => {
                if (result.ok) {
                    this.staffs.splice(this.staffs.indexOf(this.staff), 1);
                }
            })
        }
    }
});

Vue.component('staffs-list', {
    props: ['staffs'],
    data: function() {
        return {
            staff: null
        }
    },
    template:
        '<div style="position: relative; width: 1000px;">' +
        '<staff-form :staffs="staffs" :staffAttr="staff" />' +
        '<staff-row v-for="staff in staffs" :key="staff.staffid" ' +
        ':staff="staff" :staffs="staffs" :editStaff="editStaff" />' +
        '</div>',
    created: function () {
        staffApi.get().then(result =>
            result.json().then(data =>
                data.forEach(staff => this.staffs.push(staff))
            )
        )
    },
    methods: {
        editStaff: function (staff) {
            this.staff = staff;
        }
    }
});

var stafflist = new Vue({
    el: '#stafflist',
    template: '<staffs-list :staffs="staffs"/>',
    data: {
        staffs: []
    }
});