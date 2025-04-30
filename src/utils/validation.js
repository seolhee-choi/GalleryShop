export function validate({ email, password }) {
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

  return errors;
}
