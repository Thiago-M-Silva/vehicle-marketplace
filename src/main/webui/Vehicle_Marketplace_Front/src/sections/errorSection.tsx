import { IBackendErrorMessageInterface } from "@/interfaces/backendErrorMessageInterface";
import { Button } from "@/components/ui/button";
import { AlertCircle } from "lucide-react";

type Props = {
  error: IBackendErrorMessageInterface;
};

export const ErrorSection = ({ error }: Props) => {
  let title = "Erro";
  let msg = "Ocorreu um erro inesperado.";

  switch (error.status) {
    case 404:
      title = "Não Encontrado";
      msg = "Item não Encontrado!";
      break;

    case 500:
      title = "Erro do Servidor";
      msg = "Erro Interno do Servidor! Tente novamente mais tarde.";
      break;

    default:
      if (error.status) {
        title = `Erro ${error.status}`;
      }
      break;
  }

  return (
    <div className="min-h-[60vh] flex flex-col items-center justify-center bg-slate-50 px-4 py-12">
      <div className="rounded-full bg-red-100 p-4 mb-6">
        <AlertCircle className="h-12 w-12 text-red-600" />
      </div>
      <h2 className="text-3xl font-bold text-slate-900 mb-3 text-center">
        {title}
      </h2>
      <p className="text-lg text-slate-600 mb-8 text-center max-w-md">{msg}</p>
      <div className="flex flex-col sm:flex-row gap-4">
        <Button variant="outline" onClick={() => window.location.reload()}>
          Tentar Novamente
        </Button>
        <Button onClick={() => (window.location.href = "/")}>
          Voltar ao Início
        </Button>
      </div>
    </div>
  );
};
