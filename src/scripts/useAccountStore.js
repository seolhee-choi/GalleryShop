import { defineStore } from 'pinia'

export const useAccountStore = defineStore('account', {
    state: () => ({
        account: {
            id: 0
        }
    }),
    actions: {
        setAccount(id) {
            this.account.id = id
        }
    }
})