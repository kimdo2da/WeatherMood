// /users/signup
// 이메일 입력 비밀번호 닉네임 회원가입 실패메시지 등 있음!
import { motion } from "motion/react";

type SignupScreenProps = {
  signupEmail: string;
  setSignupEmail: (value: string) => void;
  signupPassword: string;
  setSignupPassword: (value: string) => void;
  signupNickname: string;
  setSignupNickname: (value: string) => void;
  signupError: string;
  isSigningUp: boolean;
  handleSignup: () => void;
  openLogin: () => void;
};

export function SignupScreen({
  signupEmail,
  setSignupEmail,
  signupPassword,
  setSignupPassword,
  signupNickname,
  setSignupNickname,
  signupError,
  isSigningUp,
  handleSignup,
  openLogin,
}: SignupScreenProps) {
  return (
    <div className="min-h-[60vh] flex items-center justify-center">
      <motion.div
        className="w-full max-w-md p-8 space-y-6"
        style={{
          background: "#FFFFFF",
          borderRadius: "30px",
          border: "4px solid #2D2D2D",
          boxShadow: "6px 6px 0px #2D2D2D",
        }}
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
      >
        <div className="text-center space-y-2">
          <h2 className="text-3xl font-medium text-gray-800">회원가입</h2>
          <p className="text-gray-600 text-sm">
            감성 처방전을 기록할 계정을 만들어보세요
          </p>
        </div>

        <div className="space-y-4">
          <input
            type="email"
            value={signupEmail}
            onChange={(e) => setSignupEmail(e.target.value)}
            placeholder="이메일"
            className="w-full px-4 py-3 outline-none"
            style={{
              borderRadius: "18px",
              border: "3px solid #2D2D2D",
            }}
          />

          <input
            type="password"
            value={signupPassword}
            onChange={(e) => setSignupPassword(e.target.value)}
            placeholder="비밀번호"
            className="w-full px-4 py-3 outline-none"
            style={{
              borderRadius: "18px",
              border: "3px solid #2D2D2D",
            }}
          />

          <input
            type="text"
            value={signupNickname}
            onChange={(e) => setSignupNickname(e.target.value)}
            placeholder="닉네임"
            className="w-full px-4 py-3 outline-none"
            style={{
              borderRadius: "18px",
              border: "3px solid #2D2D2D",
            }}
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                handleSignup();
              }
            }}
          />

          {signupError && (
            <p className="text-sm text-red-500 font-medium">{signupError}</p>
          )}

          <motion.button
            onClick={handleSignup}
            disabled={isSigningUp}
            className="w-full py-4 font-medium"
            style={{
              background: "#FFD93D",
              color: "#2D2D2D",
              borderRadius: "25px",
              border: "3px solid #2D2D2D",
              boxShadow: "4px 4px 0px #2D2D2D",
              opacity: isSigningUp ? 0.6 : 1,
            }}
            whileHover={{
              y: -2,
              boxShadow: "5px 5px 0px #2D2D2D",
            }}
            whileTap={{
              y: 1,
              boxShadow: "2px 2px 0px #2D2D2D",
            }}
          >
            {isSigningUp ? "가입 중..." : "회원가입 완료"}
          </motion.button>

          <motion.button
            onClick={openLogin}
            className="w-full py-3 font-medium"
            style={{
              background: "#FFFFFF",
              color: "#2D2D2D",
              borderRadius: "25px",
              border: "3px solid #2D2D2D",
              boxShadow: "3px 3px 0px #2D2D2D",
            }}
            whileHover={{
              y: -2,
              boxShadow: "4px 4px 0px #2D2D2D",
            }}
            whileTap={{
              y: 1,
              boxShadow: "2px 2px 0px #2D2D2D",
            }}
          >
            이미 계정이 있어요
          </motion.button>
        </div>
      </motion.div>
    </div>
  );
}