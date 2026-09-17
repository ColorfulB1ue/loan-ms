import { ref, reactive } from 'vue'

/**
 * 分页 composable
 * @param {Function} fetchApi - 获取数据的API函数
 * @param {Object} defaultParams - 默认查询参数
 */
export function usePagination(fetchApi, defaultParams = {}) {
  const loading = ref(false)
  const tableData = ref([])
  const pagination = reactive({
    page: 1,
    limit: 10,
    total: 0
  })
  const queryParams = reactive({ ...defaultParams })

  const fetchData = async () => {
    loading.value = true
    try {
      const params = {
        pageNum: pagination.page,
        pageSize: pagination.limit,
        ...queryParams
      }
      const res = await fetchApi(params)
      if (res.code === 200) {
        tableData.value = res.data.list || []
        pagination.total = res.data.total || 0
      }
    } catch (e) {
      console.error('获取数据失败:', e)
    } finally {
      loading.value = false
    }
  }

  const handlePageChange = ({ page, limit }) => {
    pagination.page = page
    pagination.limit = limit
    fetchData()
  }

  const handleSearch = () => {
    pagination.page = 1
    fetchData()
  }

  const handleReset = () => {
    Object.keys(queryParams).forEach(key => {
      queryParams[key] = defaultParams[key] || ''
    })
    pagination.page = 1
    fetchData()
  }

  return {
    loading,
    tableData,
    pagination,
    queryParams,
    fetchData,
    handlePageChange,
    handleSearch,
    handleReset
  }
}