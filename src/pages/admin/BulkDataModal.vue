<template>
  <div class="modal-backdrop">
    <div class="modal-dialog">
      <div class="modal-content">
        <!-- Header -->
        <div class="modal-header">
          <h5 class="modal-title">상품 업로드 (20개)</h5>
        </div>

        <!-- Body -->
        <div class="modal-body">
          <div class="mb-3">
            <label class="form-label"
              >1. 작가의 이름을 입력하세요 (예: Van Gogh)</label
            >
            <input v-model="author" class="form-control" />
          </div>
          <div class="mb-3 d-flex gap-2">
            <button class="btn btn-success" @click="fetchArtWorks">
              작품 데이터 다운로드
            </button>
          </div>
          <hr />
          <div class="mb-3">
            <label class="form-label">2. JSON 파일 업로드</label>
            <input
              type="file"
              @change="handleFile"
              accept=".json"
              class="form-control"
            />
          </div>
          <div class="mb-3">
            <button class="btn btn-primary" @click="uploadJson">
              데이터 DB로 전송
            </button>
          </div>
        </div>

        <!-- Footer -->
        <div class="modal-footer">
          <button
            class="btn btn-outline-secondary"
            type="button"
            @click="$emit('close-modal')"
          >
            취소
          </button>
          <button class="btn btn-primary" type="button" @click="upload">
            변경
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
// 내부 axios interceptor 설정때문에 외부 axios사용,
import axios from "axios";
import { useAlert } from "@/utils/alert.js";

const emit = defineEmits(["close-modal"]);
const author = ref("");
const file = ref(null);
const { vAlert, vSuccess } = useAlert();

const fetchArtWorks = async () => {
  try {
    //1. 전체 objectId 가져오기 - 이미지 있는것 만 필터(저작권때문에 반고흐꺼밖에안됨 ㅠㅠ)
    // const searchUrl = `https://collectionapi.metmuseum.org/public/collection/v1/search?hasImages=true&q=Van Gogh`;
    const searchUrl = `https://collectionapi.metmuseum.org/public/collection/v1/search?hasImages=true&q=${encodeURIComponent(author.value)}`;
    const { data: searchData } = await axios.get(searchUrl);

    //2. 원하는 개수만큼 objectId만 추리기(속도 고려)
    // const objectIDs = searchData.objectIDs?.slice(0, 20) || [];
    const objectIDs = searchData.objectIDs?.slice(21, 30) || []; //일단 9개만
    const results = [];

    //3. objectId 하나씩 요청
    for (const id of objectIDs) {
      try {
        const { data: artwork } = await axios.get(
          `https://collectionapi.metmuseum.org/public/collection/v1/objects/${id}`,
        );

        // 이미지가 있는 경우에만 추가
        if (artwork.primaryImageSmall) {
          results.push({
            name: artwork.title,
            imgPath: artwork.primaryImageSmall,
            price: 0,
            discountPer: 0,
          });
        }
      } catch (error) {
        console.warn(`ID ${id}에서 오류 발생, 건너뜀:`, error.message);
        continue; // 이건 생략 가능 – catch 되면 자동으로 다음 루프로 넘어감
      }
    }

    //4. JSON 파일로 저장
    downloadJson(results, `${author.value}_works`);
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

// 6. 다운로드 받은 json파일을 업로드하면 items 테이블에 insert 처리
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

<style scoped>
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1050;
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-dialog {
  max-width: 600px;
  width: 100%;
}

.modal-content {
  background: white;
  border-radius: 0.5rem;
  padding: 1rem;
}

.modal-btn {
  margin-top: 1rem;
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
}

.modal-content > div {
  margin-bottom: 1rem;
  padding-top: 2rem;
  font-size: 1rem;
  color: #333;
}

.modal-content input[type="text"],
.modal-content input[type="file"] {
  display: block;
  margin-top: 0.5rem;
  margin-bottom: 1rem;
  padding: 0.5rem 0.75rem;
  width: 100%;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  transition: border-color 0.3s ease;
}

.modal-content input[type="text"]:focus,
.modal-content input[type="file"]:focus {
  border-color: #007bff;
  outline: none;
}

.modal-content button:hover {
  background-color: #0056b3;
  color: white;
}

/* 개별 버튼 스타일 조절 (예: fetch 버튼, upload 버튼) */
.modal-content > div > button {
  margin-top: 0.25rem;
}

.modal-footer {
  margin-top: 1rem;
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
}

/* 구분선 */
hr {
  border: none;
  border-top: 1px solid #eee;
  margin: 1rem 0;
}
</style>
