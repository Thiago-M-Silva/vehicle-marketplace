export interface IBackendErrorMessageInterface {
  status: number,
  error: string,
  code: string,
  message: string,
  path: string,
  timestamp: string,
}