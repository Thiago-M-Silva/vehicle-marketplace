import { useNavigate } from "react-router";
import { Button } from "@/components/ui/button";

export const NotFountError = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-[60vh] flex items-center justify-center px-4">
      <div className="w-full max-w-2xl bg-white rounded-lg shadow p-8 text-center">
        <div className="flex items-center justify-center">
          <svg
            width="120"
            height="120"
            viewBox="0 0 24 24"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
            className="text-slate-300"
          >
            <path d="M11 7h2v6h-2z" fill="currentColor" opacity="0.9" />
            <path d="M11 15h2v2h-2z" fill="currentColor" opacity="0.9" />
            <path
              d="M12 2a10 10 0 100 20 10 10 0 000-20zM12 20a8 8 0 110-16 8 8 0 010 16z"
              fill="currentColor"
              opacity="0.06"
            />
          </svg>
        </div>

        <h2 className="mt-4 text-2xl font-semibold text-slate-900">
          404 — Página não encontrada
        </h2>
        <p className="mt-2 text-sm text-slate-600">
          Não conseguimos encontrar o que você procura. Verifique a URL ou volte
          para a página inicial.
        </p>

        <div className="mt-6 flex flex-col sm:flex-row items-center justify-center gap-3">
          <Button onClick={() => navigate("/")} className="w-full sm:w-auto">
            Ir para início
          </Button>
          <Button
            variant="outline"
            onClick={() => navigate(-1)}
            className="w-full sm:w-auto"
          >
            Voltar
          </Button>
        </div>
      </div>
    </div>
  );
};
