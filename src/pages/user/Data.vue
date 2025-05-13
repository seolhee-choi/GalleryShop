<template>
  <div>
    <button @click="fetchArtWorks">작품 데이터 다운로드</button>
    <hr />
    <input type="file" @change="handleFile" accept=".json" />
    <button @click="uploadJson">데이터 db로 전송</button>
  </div>
</template>

<script setup>
import { ref } from "vue";
import axios from "@/axios.js";

const file = ref(null);
const fetchArtWorks = async () => {
  try {
    //1. 전체 objectId 가져오기 - 이미지 있는것 만 필터
    const searchUrl = `https://collectionapi.metmuseum.org/public/collection/v1/search?hasImages=true&q=Van Gogh`;
    const { data: searchData } = await axios.get(searchUrl);

    //2. 50개의 objectId만 추리기(속도 고려)
    const objectIDs = searchData.objectIDs?.slice(0, 20) || [];
    const results = [];

    //3. objectId 하나씩 요청
    for (const id of objectIDs) {
      const { data: artwork } = await axios.get(
        `https://collectionapi.metmuseum.org/public/collection/v1/objects/${id}`,
      );

      //3-1. result 배열에 데이터 담기
      results.push({
        // objectID: artwork.objectID,
        name: artwork.title,
        imgPath: artwork.primaryImageSmall,
      });
    }

    //4. JSON 파일로 저장
    downloadJson(results, "van_gogh_works");
  } catch (err) {
    console.error("에러 발생", err);
  }
};
//5. JSON 다운로드 유틸
const downloadJson = (data, filename = "data.json") => {
  const jsonStr = JSON.stringify(data, null, 2);
  const blob = new Blob([jsonStr], { type: "application/json" });
  const url = URL.createObjectURL(blob);

  const link = document.createElement("a");
  link.href = url;
  link.download = filename;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};

const handleFile = (e) => {
  file.value = e.target.files[0];
};

const uploadJson = async () => {
  if (!file.value) return vAlert("파일을 선택해주세요");

  const formData = new FormData();
  formData.append("file", file.value);

  try {
    await axios.post("/api/items/upload", formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });
    alert("업로드 성공");
  } catch (err) {
    console.error(err);
    alert("업로드 실패");
  }
};
</script>

<style lang="scss" scoped></style>
