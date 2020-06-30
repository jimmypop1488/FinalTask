var staffApi = Vue.resource('/staff/tree')

Vue.component('staff-row', {
    props: ['staff', 'staffs'],
    template: '<div>' +
        '{{ staff.staffname }}' +
        '</div>',
});

Vue.component('staffs-tree', {
    props: ['staffs'],
    data: function() {
        return {
            staff: null
        }
    },
    template:
        '<div>' +
        '<staff-row v-for="staff in staffs" :key="staff.staffid" ' +
        ':staff="staff" :staffs="staffs"/>' +
        '</div>',
    created: function () {
        staffApi.get().then(result =>
            result.json().then(data =>
                data.forEach(staff => this.staffs.push(staff))
            )
        )
    },
});

var stafftree = new Vue({
    el: '#stafftree',
    template: '<staffs-tree :staffs="staffs"/>',
    data: {
        staffs: []
    }
});