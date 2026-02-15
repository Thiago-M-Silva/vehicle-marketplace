import Lottie from "react-lottie";
import animationData from "../assets/loading/Horse_Walk_Loop.json";

export const LoadingSection = () => {
  const defaultOptions = {
    loop: true,
    autoplay: true,
    animationData: animationData,
    rendererSettings: {
      preserveAspectRatio: "xMidYMid slice",
    },
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-slate-50">
      <div className="pointer-events-none">
        <Lottie options={defaultOptions} height={300} width={300} />
      </div>
      <h2 className="mt-4 text-xl font-semibold text-slate-700 animate-pulse">
        Loading...
      </h2>
    </div>
  );
};
