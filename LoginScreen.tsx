//POST /users/login
//이메일,비밀번호,로그인,실패메시지등이 있음!
import { motion } from "motion/react";

type LoginScreenProps = {
  loginEmail: string;
  setLoginEmail: (value: string) => void;
  loginPassword: string;
  setLoginPassword: (value: string) => void;
  loginError: string;
  handleLogin: () => void;
  openSignup: () => void;
};

export function LoginScreen({
  loginEmail,
  setLoginEmail,
  loginPassword,
  setLoginPassword,
  loginError,
  handleLogin,
  openSignup,
}: LoginScreenProps) {
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
          <h2 className="text-3xl font-medium text-gray-800">로그인</h2>
          <p className="text-gray-600 text-sm">
            감성 테스트 결과를 저장하려면 로그인이 필요해요
          </p>
        </div>

        <div className="space-y-4">
          <input
            type="email"
            value={loginEmail}
            onChange={(e) => setLoginEmail(e.target.value)}
            placeholder="이메일"
            className="w-full px-4 py-3 outline-none"
            style={{
              borderRadius: "18px",
              border: "3px solid #2D2D2D",
            }}
          />

          <input
            type="password"
            value={loginPassword}
            onChange={(e) => setLoginPassword(e.target.value)}
            placeholder="비밀번호"
            className="w-full px-4 py-3 outline-none"
            style={{
              borderRadius: "18px",
              border: "3px solid #2D2D2D",
            }}
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                handleLogin();
              }
            }}
          />

          {loginError && (
            <p className="text-sm text-red-500 font-medium">{loginError}</p>
          )}

          <motion.button
            onClick={handleLogin}
            className="w-full py-4 font-medium"
            style={{
              background: "#FFD93D",
              color: "#2D2D2D",
              borderRadius: "25px",
              border: "3px solid #2D2D2D",
              boxShadow: "4px 4px 0px #2D2D2D",
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
            로그인하기
          </motion.button>

          <motion.button
            onClick={openSignup}
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
            회원가입하기
          </motion.button>
        </div>
      </motion.div>
    </div>
  );
}