//GET /contents/popular
// api에서 받아온 인기/추천 콘텐츠를 처방전 화면으로 표시
import { motion } from "motion/react";
import { CozyCard } from "../components/CozyCard";
import { CozyFairy } from "../components/CozyFairy";
import { PillIcon } from "../components/icons/PillIcon";
import type { CardContent } from "../types";

type PrescriptionScreenProps = {
  recommendedContent: CardContent[];
};

export function PrescriptionScreen({
  recommendedContent,
}: PrescriptionScreenProps) {
  return (
    <div className="space-y-10">
      <div className="text-center space-y-6">
        <motion.div
          className="inline-block"
          animate={{ rotate: [0, 10, -10, 0] }}
          transition={{ duration: 3, repeat: Infinity }}
        >
          <PillIcon className="w-24 h-24 mx-auto" />
        </motion.div>

        <h2 className="text-4xl font-medium text-gray-800">
          오늘의 감성 처방전
        </h2>

        <p className="text-gray-600 text-lg max-w-2xl mx-auto">
          현재 날씨와 당신의 감정을 분석해 완벽한 콘텐츠를 처방해드립니다
        </p>
      </div>

      <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
        {recommendedContent.map((content) => (
          <CozyCard
            key={`prescription-${content.type}-${content.title}`}
            {...content}
          />
        ))}
      </div>

      <CozyFairy
        message="비 오는 날에는 감성적인 작품이 더 깊게 다가와요. 천천히 감상하면서 여유를 즐겨보세요."
        mood="thoughtful"
      />
    </div>
  );
}