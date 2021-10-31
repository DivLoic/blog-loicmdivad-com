import ora from 'ora';

export const initSpinner = (subs) => { return ora('Listen for new records from: ' + subs)};
export const startSpinner = (spiner) => spiner.start()
export const stopSpinner = (spiner) => spiner.stop()
