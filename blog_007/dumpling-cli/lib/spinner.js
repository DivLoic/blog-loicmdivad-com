import ora from 'ora';

export const initSpinner = () => { return ora('Listen for new records from the test subscription')};
export const startSpinner = (spiner) => spiner.start()
export const stopSpinner = (spiner) => spiner.stop()
