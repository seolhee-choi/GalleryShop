import { useAlertStore } from "@/scripts/alert";
import { storeToRefs } from "pinia";

export const useAlert = () => {
  const { alerts } = storeToRefs(useAlertStore());
  const { vAlert, vSuccess } = useAlertStore();
  return {
    alerts,
    vAlert,
    vSuccess,
  };
};

export const confirmAndSaveChanges = async (data) => {
  const isConfirmed = window.confirm("정말로 저장하시겠습니까?");
  return isConfirmed;
};
