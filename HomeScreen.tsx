//GET /weather/current
//GET /contents/popular 
// 현재 날씨 메시지, 인기작품카드, 오늘의 처방전,모두보기가 있음!
import { motion } from "motion/react";
import { CozyFairy } from "../components/CozyFairy";
import { CozyCard } from "../components/CozyCard";
import { PillIcon } from "../components/icons/PillIcon";
import type { CardContent } from "../types";

type HomeScreenProps = {
  weatherMessage: string;
  recommendedContent: CardContent[];
  openPrescription: () => void;
  openContents: () => void | Promise<void>;
};

export function HomeScreen({
  weatherMessage,
  recommendedContent,
  openPrescription,
  openContents,
}: HomeScreenProps) {
  return (
    <div className="space-y-12">
      <CozyFairy message={weatherMessage} mood="calm" />

      <div className="grid grid-cols-1 md:grid-cols-1 gap-6">
        <motion.button
          className="p-8 text-left group"
          style={{
            background: "#FFFFFF",
            borderRadius: "30px",
            border: "4px solid #2D2D2D",
            boxShadow: "6px 6px 0px #2D2D2D",
          }}
          whileHover={{
            y: -4,
            boxShadow: "8px 8px 0px #2D2D2D",
          }}
          whileTap={{
            y: 2,
            boxShadow: "3px 3px 0px #2D2D2D",
          }}
          onClick={openPrescription}
        >
          <div className="space-y-4">
            <div
              className="w-16 h-16 rounded-full flex items-center justify-center"
              style={{
                background: "#FFD93D",
                border: "3px solid #2D2D2D",
              }}
            >
              <PillIcon className="w-10 h-10" />
            </div>

            <div>
              <h3 className="text-xl font-medium text-gray-800 mb-1">
                오늘의 처방전
              </h3>
              <p className="text-gray-600 text-sm">당신을 위한 맞춤 추천</p>
            </div>
          </div>
        </motion.button>
      </div>

      <div className="space-y-6">
        <div className="flex items-center justify-between">
          <h2 className="text-3xl font-medium text-gray-800">인기 작품</h2>

          <motion.button
            onClick={openContents}
            className="font-medium flex items-center gap-1 px-4 py-2"
            style={{
              background: "#FFD93D",
              color: "#2D2D2D",
              borderRadius: "20px",
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
            모두 보기
            <motion.span
              animate={{ x: [0, 5, 0] }}
              transition={{ duration: 1.5, repeat: Infinity }}
            >
              →
            </motion.span>
          </motion.button>
        </div>

        <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
          {recommendedContent.map((content) => (
            <CozyCard key={`${content.type}-${content.title}`} {...content} />
          ))}
        </div>
      </div>
    </div>
  );
}