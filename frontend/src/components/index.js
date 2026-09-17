import PasswordDialog from './PasswordDialog.vue'
import PageHeader from './PageHeader.vue'
import StatusTag from './StatusTag.vue'
import Pagination from './Pagination.vue'
import TableContainer from './TableContainer.vue'

export {
  PasswordDialog,
  PageHeader,
  StatusTag,
  Pagination,
  TableContainer
}

// 全局注册
export default {
  install(app) {
    app.component('PasswordDialog', PasswordDialog)
    app.component('PageHeader', PageHeader)
    app.component('StatusTag', StatusTag)
    app.component('Pagination', Pagination)
    app.component('TableContainer', TableContainer)
  }
}