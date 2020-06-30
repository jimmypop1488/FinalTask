var organizationApi = Vue.resource('/organization/tree')

Vue.component('organization-row', {
    props: ['organization', 'organizations'],
    template: '<div>' +
        '{{ organization.orgname }}' +
        '</div>',
});

Vue.component('organizations-tree', {
    props: ['organizations'],
    data: function() {
        return {
            organization: null
        }
    },
    template:
        '<div>' +
        '<organization-row v-for="organization in organizations" :key="organization.orgid" ' +
        ':organization="organization" :organizations="organizations"/>' +
        '</div>',
    created: function () {
        organizationApi.get().then(result =>
            result.json().then(data =>
                data.forEach(organization => this.organizations.push(organization))
            )
        )
    },
});

var orgtree = new Vue({
    el: '#orgtree',
    template: '<organizations-tree :organizations="organizations"/>',
    data: {
        organizations: []
    }
});