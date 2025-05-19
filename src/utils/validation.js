export function validate({ email, password, newPassword = "" }) {
  const errors = {};

  //이메일 유효성 검사(- _ 특수문자가 가능/ 중앙에 @ 필수/ 뒤에 2~3글자 필요)
  const emailRegex =
    /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,}$/i;
  if (email.trim() === "") {
    errors.email = "이메일을 입력해주세요.";
  } else if (!emailRegex.test(email)) {
    errors.email = "올바른 이메일 형식을 입력해주세요";
  }

  //비밀번호 유효성 검사(A~Z, a~z, 0~9로 시작하는 9~12 자리 비밀번호 설정 가능)
  const passwordRegex = /^[A-Za-z0-9]{9,12}$/;
  if (password.trim() === "") {
    errors.password = "비밀번호를 입력해주세요.";
  } else if (!passwordRegex.test(password)) {
    errors.password = "비밀번호는 9자리 이상의 영문+숫자 조합이어야 합니다.";
  }

  //비밀번호 유효성 검사(A~Z, a~z, 0~9로 시작하는 9~12 자리 비밀번호 설정 가능)
  const newPasswordRegex = /^[A-Za-z0-9]{9,12}$/;
  if (newPassword.trim() === "") {
    errors.newPassword = "새로 변경할 비밀번호를 입력해주세요.";
  } else if (!passwordRegex.test(newPassword)) {
    errors.newPassword = "비밀번호는 9자리 이상의 영문+숫자 조합이어야 합니다.";
  }

  return errors;
}

// 이름 유효성 검사 함수
export function validateNameFormat(name) {
  if (!name || name.trim().length === 0) {
    return "이름을 입력해주세요.";
  }

  // 중간 공백 제거 (앞뒤 trim은 이미 위에서 처리됨)
  if (/\s/.test(name)) {
    return "이름에 공백을 포함할 수 없습니다.";
  }

  // 한글, 영문만 허용 (공백 포함), 숫자 및 특수문자 금지
  const nameRegex = /^[가-힣a-zA-Z\s]+$/;
  if (!nameRegex.test(name)) {
    return "이름에는 숫자나 특수문자를 사용할 수 없습니다.";
  }

  return null; // 통과
}

// 카드 유효성 검사 함수
export function cardSerialNumberFormatter(value) {
  if (/[^0-9]/.test(value)) {
    return { formatted: "", error: "카드번호는 숫자만 입력할 수 있습니다." };
  }

  const raw = value.replace(/[^0-9]/g, ""); // 숫자만 남김

  if (raw.length > 16) {
    return {
      formatted: raw.slice(0, 16),
      error: "카드 번호는 16자리를 초과할 수 없습니다.",
    };
  }

  if (raw.length < 16) {
    return {
      formatted: raw,
      error: "카드 번호는 16자리여야 합니다.",
    };
  }

  return { formatted: raw, error: null };
}

// 주소 유효성 검사 함수
export function validateAddressFormat(address) {
  if (!address || address.trim().length < 5) {
    return "주소를 올바르게 입력해주세요.";
  }

  if (/[^a-zA-Z0-9가-힣\s\-.,]/.test(address)) {
    return "주소에 유효하지 않은 문자가 포함되어 있습니다.";
  }

  return null; // 통과
}
