import { onBeforeMount } from "vue";
import { usePaginationStore } from "@/scripts/usePaginationStore.js";

export function useInitPagination(loadFunc) {
  const pagination = usePaginationStore();

  onBeforeMount(() => {
    pagination.reset();
    pagination.changePage(1);
    loadFunc();
  });
}
