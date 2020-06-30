function getIndex(list, orgid) {
    for (var i = 0; i < list.length; i++) {
        if (list[i].orgid === orgid) {
            return i;
        }
    }

    return -1;
}

var organizationApi = Vue.resource('/organization{/orgid}')

Vue.component('organization-form', {
    props: ['organizations', 'organizationAttr'],
    data: function () {
        return {
            orgid: '',
            orgname: '',
            headorgid: ''
        }
    },
    watch: {
        organizationAttr: function(newVal, oldVal) {
            this.orgid = newVal.orgid;
            this.orgname = newVal.orgname;
            this.headorgid = newVal.headorgid;
        }
    },
    template: '<table cellspacing="0" cellpadding="0"><tr><td><table cellspacing="0" cellpadding="0"><tr>' +
        '<td><input type="text" placeholder="Организация" v-model="orgname" /></td>' +
        '<td><input type="text" placeholder="Головная Организация" v-model="headorgid" /></td>' +
        '<td><input type="button" value="Сохранить" @click="save" /></td>' +
        '</tr></table>' +
        '<table bordercolor="black" border="1px" cellspacing="0" cellpadding="0"><tr>' +
        '<td width="100px"><b>№</b></td>' +
        '<td width="200px"><b>Название Организации</b></td>' +
        '<td width="200px"><b>Название Головной Организации</b></td>' +
        '<td width="100px"><b>Количество Работников</b></td>' +
        '<td width="145px"><b></b></td>' +
        '</tr></table></td></tr></table>',
    methods: {
        save: function () {
            var organization = {
                orgid: "none",
                orgname: this.orgname,
                headorgid: this.headorgid
            };
            console.log(organization);
            if (this.orgid) {
                organizationApi.update({orgid: this.orgid}, organization).then(result =>
                    result.json().then(data => {
                        var index = getIndex(this.organizations, data.orgid);
                        this.organizations.splice(index, 1, data);
                        this.orgid = '';
                        this.orgname = '';
                        this.headorgid = ''
                    })
                )
            } else {
                organizationApi.save({}, organization);
                window.location.reload();
            }
        }
    }
});

Vue.component('organization-row', {
    props: ['organization', 'organizations', 'editOrganization'],
    data: function() {
        return {
            ind: getIndex(this.organizations, this.organization.orgid) + 1
        }
    },
    template: '<table bordercolor="black" border="1px" cellspacing="0" cellpadding="0">' +
        '<tr><td width="100px">{{ ind }}</td>' +
        '<td width="200px">{{ organization.orgname }}</td>' +
        '<td width="200px">{{ organization.headorgid }}</td>' +
        '<td width="100px">{{ organization.staffCount }}</td>' +
        '<td width="145px">' +
        '<input type="button" value="Изменить" @click="edit" />' +
        '<input type="button" value="Удалить" @click="del" /></td>' +
        '</tr></table>',
    methods: {
        edit: function () {
            this.editOrganization(this.organization);
        },
        del: function () {
            organizationApi.remove({orgid: this.organization.orgid}).then(result => {
                if (result.ok) {
                    this.organizations.splice(this.organizations.indexOf(this.organization), 1);
                }
            })
        }
    }
});

Vue.component('organizations-list', {
    props: ['organizations'],
    data: function() {
        return {
            organization: null
        }
    },
    template:
        '<div style="position: relative; width: 1000px;">' +
            '<organization-form :organizations="organizations" :organizationAttr="organization" />' +
            '<organization-row v-for="organization in organizations" :key="organization.orgid" ' +
                ':organization="organization" :organizations="organizations" :editOrganization="editOrganization" />' +
        '</div>',
    created: function () {
        organizationApi.get().then(result =>
            result.json().then(data =>
                data.forEach(organization => this.organizations.push(organization))
            )
        )
    },
    methods: {
        editOrganization: function (organization) {
            this.organization = organization;
        }
    }
});

var orglist = new Vue({
    el: '#orglist',
    template: '<organizations-list :organizations="organizations"/>',
    data: {
        organizations: []
    }
});