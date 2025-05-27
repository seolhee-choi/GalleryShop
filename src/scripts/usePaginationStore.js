import { defineStore } from "pinia";

export const usePaginationStore = defineStore("pagination", {
  state: () => ({
    params: {
      page: 1,
      recordSize: 10,
      pageSize: 10,
      keyword: "",
      searchType: "",
    },
    totalPage: 1,
    totalCount: 0,
  }),
  actions: {
    reset() {
      this.page = 1;
      this.keyword = "";
      this.searchType = "";
    },
    changePage(newPage) {
      if (newPage >= 1 && newPage <= this.totalPage) {
        this.params.page = newPage;
      }
    },
    updateParams(newParams) {
      this.params = { ...this.params, ...newParams };
    },
    setPaginationInfo({ page, totalPage, totalCount }) {
      this.params.page = page;
      this.totalPage = totalPage;
      this.totalCount = totalCount;
    },
  },
});
