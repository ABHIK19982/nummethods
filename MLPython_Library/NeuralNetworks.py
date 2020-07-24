from ClassBases.BaseClassifier import ClassifierBase
import numpy as np
from sklearn.model_selection import train_test_split


class NNClassifier(ClassifierBase):
    def __init__(self,layer_dims = [50],solver='sgd',batch = None,learning_rate = 1.0,beta = 0.0,
                 func = ['relu'],C = 0.0001,regularization = 'l2',validation = 1,max_iter = 1000):
        ClassifierBase.__init__(self,solver,batch,learning_rate,beta,C,regularization,validation,max_iter)
        self.__layer_dims = layer_dims
        self.__func = func
        self.__layers = 0
        self.__val_loss = 0.0
        self.__classes = 0
        
    def __initialize_parameters(self):
        np.random.seed(3)
        
        self.__layers = len(self.__layer_dims) + 1
        self.__layer_dims.append(self.__classes)
        
        for i in range(1,self.__layers):
            #print(i)
            self._params['W' + str(i)] = np.random.randn(self.__layer_dims[i],self.__layer_dims[i-1]) * 0.01
            self._params['b' + str(i)] = np.zeros((self.__layer_dims[i],1))
            
            if self._beta != None:
                self._params['Vw' + str(i)] = np.zeros((self.__layer_dims[i],self.__layer_dims[i-1]))
                self._params['Vb' + str(i)] = np.zeros((self.__layer_dims[i],1))
        
    def __compute_cost(self,A,Y):
        temp = 0
        m = Y.shape[1]
        
        for i in range(1,self.__layers):
            W = self._params['W' + str(i)]
            temp += (self._C /2) * np.sum(np.sum(W**2,axis = 1,keepdims = True))
            temp /= m
        
        val = -1 * np.sum(np.sum(np.multiply(np.log(A),Y),axis = 1,keepdims = True)) * (1/m)
        val = val+temp
        
        return val
    
    def __forward_propagation(self,X):
        cache = {}
        A = X
        cache['A0'] = A
        for i in range(1 , self.__layers):
            Z = np.matmul(self._params['W' + str(i)],A) + self._params['b' + str(i)]
            A = self.activation(Z,self.__func[i-1])
            cache['A' + str(i)] = A
            cache['Z' + str(i)] = Z
        
        return A,cache
    
    def __backward_propagation(self,A,Y,cache):
        m = Y.shape[1]
        grads = {}
        dZ = A-Y
        
        for i in range(self.__layers-1,0,-1):
            An = cache['A' + str(i-1)]
            dW =(self._C * self._params['W' + str(i)]) + np.matmul(dZ,An.T) * 1/m
            db = np.sum(dZ,axis = 1,keepdims = True) * 1/m
            
            dA = np.matmul(self._params['W' + str(i)].T,dZ)
            if(i!=1):
                dZ = np.multiply(dA , self.d_activation(An,self.__func[i-2]))
            
            if(self._beta != None):
                grads['dw' + str(i)] = (self._beta * self._params['Vw' + str(i)]) + (1-self._beta) * dW
                grads['db' + str(i)] = (self._beta * self._params['Vb' + str(i)]) + (1-self._beta) * db
            else:
                grads['dw' + str(i)] = dW
                grads['db' + str(i)] = db
       
        return grads
    
    def __update_params(self,grads):
        
        for i in np.arange(1,self.__layers,1):
             #print(grads.keys())
            self._params['W' + str(i)] -= self._alpha * grads['dw' + str(i)]
            self._params['b' + str(i)] -= self._alpha * grads['db' + str(i)]
            
            if self._beta != None:
                self._params['Vw' + str(i)] = grads['dw' + str(i)]
                self._params['Vb' + str(i)] = grads['db' + str(i)]

    def __train(self,X_t,Y_t,X_v,Y_v,print_cost):
        self.__layer_dims.insert(0,X_t.shape[0])
        print(self.__layer_dims)
        self.__initialize_parameters()
        
        idx = []
        
        if(self._batch_size !=None):
            idx = np.arange(0,X_t.shape[1],self._batch_size)
            np.random.shuffle(idx)
        j = 0
        
        for i in range(self._max_iter):
            X_n = X_t
            Y_n = Y_t
            if(self._batch_size == None):
                pos = np.random.choice(np.arange(X_t.shape[1]))
                X_n = X_t[:,pos].reshape(-1,1)
                Y_n = Y_t[:,pos].reshape(-1,1)
            else :
                pos = idx[j]
                X_n = X_t[:,pos:min(pos+self._batch_size,X_t.shape[1])]
                Y_n = Y_t[:,pos:min(pos+self._batch_size,X_t.shape[1])]
            
            A,cache = self.__forward_propagation(X_n)
            self.__loss = self.__compute_cost(A,Y_n)
            Aval,cacheval = self.__forward_propagation(X_v)
            self.__val_loss = self.__compute_cost(Aval,Y_v)
            
            grads = self.__backward_propagation(A,Y_n,cache)
            self.__update_params(grads)
            if(print_cost and i % 1000 == 0):
                print('epoch {0}: loss {1},validation loss {2}'.format(i,self.__loss,self.__val_loss))
                

    def toCategorical(self,Y):
        self.__classes = len(set(list(Y.T[0])))
        
        cmat = np.zeros((Y.shape[0],self.__classes))
        for i in range(Y.shape[0]):
            cmat[i,Y[i]] = 1
        
        return cmat
    
    def fit(self,X,Y,v_size = 0.15,print_cost = True):
        Y = self.toCategorical(Y)
        X_train,X_val,Y_train,Y_val = train_test_split(X,Y,stratify = Y,
                                                       random_state = 42,test_size = v_size)
        
        self.__train(X_train.T,Y_train.T,X_val.T,Y_val.T,print_cost)
    def getParams(self):
        return self._params
    def getLoss(self):
        return self.__loss
    def getClasses(self):
        return self.__classes
    def setClasses(self,classes):
        self.__classes = classes
    params_ = property(getParams)
    loss_ = property(getLoss)
    classes_ = property(getClasses,setClasses)
    
    

        
